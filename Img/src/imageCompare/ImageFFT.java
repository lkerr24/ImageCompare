package imageCompare;
package imageCompare;

/*========================================================== ImageFFT.java ===*/
/*                                                                            */
/*  Computes the FFT of an image, and the inverse FFT of its frequency        */
/*  domain representation.  The transformed data can be inspected or          */
/*  filtered before performing the inverse transform.                         */
/*                                                                            */
/*---------------------------------------------------------------- package ---*/

package img.transform.fourier;
//package com.pearsoneduc.ip.op;

/*----------------------------------------------------------------- import ---*/

import java.awt.image.*;
import java.awt.color.ColorSpace;

import img.image.*;
//import com.pearsoneduc.ip.util.Complex;

/*============================================================== Image FFT ===*/

public class ImageFFT {

  public  static final int    NO_WINDOW       = 1;
  public  static final int    BARTLETT_WINDOW = 2;
  public  static final int    HAMMING_WINDOW  = 3;
  public  static final int    HANNING_WINDOW  = 4;
  private static final String NO_DATA         = "no spectral data available";
  private static final String INVALID_PARAMS  = "invalid filter parameters";
  private static final double TWO_PI          = 2.0*Math.PI;

  private Complex[] data;            // Complex storage for results of FFT
  private int       log2w;           // base-2 logarithm of transform width
  private int       log2h;           // base-2 logarithm of transform height
  private int       width;           // Width of transform
  private int       height;          // Height of transform
  private int       window;          // Windowing function applied to image data
  private boolean   spectral = false;// spectral or spatial data



 /*-------------------------------------------------------- bartlettWindow ---*/

  public static final double bartlettWindow(double r, double rmax) {

         /* Computes one half of a radial Bartlett windowing function. */
         /* @param r distance from centre of data                      */
         /* @param rmax maximum distance                               */
         /* @return function value.                                    */

    return 1.0 - Math.min(r, rmax)/rmax;
  }

  /*-------------------------------------------------------- hammingWindow ---*/

  public static final double hammingWindow(double r, double rmax) {

         /* Computes one half of a radial Hamming windowing function. */
         /* @param r distance from centre of data                     */
         /* @param rmax maximum distance                              */
         /* @return function value.                                   */

    double f = (rmax - Math.min(r, rmax)) / rmax;
    return 0.54 - 0.46*Math.cos(f*Math.PI);
  }

  /*-------------------------------------------------------- hammingWindow ---*/

  public static final double hanningWindow(double r, double rmax) {

         /* Computes one half of a radial Hanning windowing function. */
         /* @param r distance from centre of data                     */
         /* @param rmax maximum distance                              */
         /* @return function value.                                   */

    double f = (rmax - Math.min(r, rmax)) / rmax;
    return 0.5 - 0.5*Math.cos(f*Math.PI);
  }

  /*------------------------------------------- butterworthLowPassFunction ---*/

  public static final double butterworthLowPassFunction(int n, double radius, double r) {

         /* Computes the transfer function for a Butterworth low pass filter. */
         /* @param n order of filter                                          */
         /* @param radius filter radius                                       */
         /* @param r distance from centre of spectrum                         */
         /* @return transfer function value.                                  */

    double p = Math.pow(r/radius, 2.0*n);
    return 1.0/(1.0 + p);
  }

  /*------------------------------------------ butterworthHighPassFunction ---*/

  public static final double butterworthHighPassFunction(int n, double radius, double r) {

     /* Computes the transfer function for a Butterworth high pass filter. */
     /* @param n order of filter                                           */
     /* @param radius filter radius                                        */
     /* @param r distance from centre of spectrum                          */
     /* @return transfer function value.                                   */

    try {
      double p = Math.pow(radius/r, 2.0*n);
      return 1.0/(1.0 + p);
    }
    catch (ArithmeticException e) {
      return 0.0;
    }
  }

  /*------------------------------------------ butterworthBandPassFunction ---*/

  public static final double butterworthBandPassFunction(int n, double radius, double delta, double r) {

     /* Computes the transfer function for a Butterworth band pass filter. */
     /* @param n order of filter                                           */
     /* @param radius filter radius                                        */
     /* @param delta band width                                            */
     /* @param r distance from centre of spectrum                          */
     /* @return transfer function value.                                   */

    return 1.0-butterworthBandStopFunction(n, radius, delta, r);
  }

  /*------------------------------------------ butterworthBandStopFunction ---*/

  public static final double butterworthBandStopFunction(int n, double radius, double delta, double r) {

     /* Computes the transfer function of a Butterworth band stop filter. */
     /* @param n order of filter                                          */
     /* @param radius filter radius                                       */
     /* @param delta band width                                           */
     /* @param r distance from centre of spectrum                         */
     /* @return transfer function value.                                  */

    try {
      double p = Math.pow(delta*radius/(r*r - radius*radius), 2.0*n);
      return 1.0/(1.0 + p);
    }
    catch (ArithmeticException e) {
      return 0.0;
    }
  }

  /*------------------------------------------------------------- ImageFFT ---*/

  public ImageFFT(BufferedImage image) throws FFTException {

         /* Creates an ImageFFT for the specified image.  There will be  */
         /* no windowing of image data.                                  */
         /* @param image input image                                     */
         /* @exception FFTException if the image is not 8-bit greyscale. */

    this(image,NO_WINDOW);
  }

  /*------------------------------------------------------------- ImageFFT ---*/

  public ImageFFT(BufferedImage img, int win) throws FFTException {

         /* Creates an ImageFFT for the specified image, applying the    */
         /* specified windowing function to the data.                    */
         /* @param image input image                                     */
         /* @param win windowing function                                */
         /* @exception FFTException if the image is not 8-bit greyscale. */

    System.out.println("Inside FFT");
    BufferedImage image = img;
    if (img.getType() != BufferedImage.TYPE_BYTE_GRAY) {
      //System.err.println("Converting colour image to greyscale image...");
      ColorConvertOp op = new ColorConvertOp(
       ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
      BufferedImage greyImage = op.filter(img, null);
      image = greyImage;
    }
    if (image.getType() != BufferedImage.TYPE_BYTE_GRAY)
      throw new FFTException("image must be 8-bit greyscale");

    log2w = powerOfTwo(image.getWidth());        // Compute dimensions, allowing for zero padding
    log2h = powerOfTwo(image.getHeight());
    width = 1 << log2w;
    height = 1 << log2h;
    window = win;

    data = new Complex[width*height];     // Allocate storage for results of FFT
    for (int i = 0; i < data.length; ++i)
      data[i] = new Complex();

    Raster raster = image.getRaster();
    double xc = image.getWidth()/2.0, yc = image.getHeight()/2.0;
    double r, rmax = Math.min(xc, yc);
    switch (window) {

      case HAMMING_WINDOW:
        for (int y = 0; y < image.getHeight(); ++y)
          for (int x = 0; x < image.getWidth(); ++x) {
            r = Math.sqrt((x-xc)*(x-xc) + (y-yc)*(y-yc));
            data[y*width+x].re =
             (float) (hammingWindow(r, rmax)*raster.getSample(x, y, 0));
          }
        break;

      case HANNING_WINDOW:
        for (int y = 0; y < image.getHeight(); ++y)
          for (int x = 0; x < image.getWidth(); ++x) {
            r = Math.sqrt((x-xc)*(x-xc) + (y-yc)*(y-yc));
            data[y*width+x].re =
             (float) (hanningWindow(r, rmax)*raster.getSample(x, y, 0));
          }
        break;

      case BARTLETT_WINDOW:
        for (int y = 0; y < image.getHeight(); ++y)
          for (int x = 0; x < image.getWidth(); ++x) {
            r = Math.sqrt((x-xc)*(x-xc) + (y-yc)*(y-yc));
            data[y*width+x].re =
             (float) (bartlettWindow(r, rmax)*raster.getSample(x, y, 0));
          }
        break;

      default:  // NO_WINDOW
        for (int y = 0; y < image.getHeight(); ++y)
          for (int x = 0; x < image.getWidth(); ++x)
            data[y*width+x].re = raster.getSample(x, y, 0);
        break;

    }
  }

  /*------------------------------------------------------------- getWidth ---*/

  public int getWidth() {              // @return width of FFT
    return width;
  }

  /*------------------------------------------------------------ getHeight ---*/

  public int getHeight() {             // @return height of FFT
    return height;
  }

  /*-------------------------------------------------------- getWindow ---*/

  public int getWindow() {             // @return current windowing function
    return window;
  }

  /*----------------------------------------------------------- isSpectral ---*/

  public boolean isSpectral() {        // true: spectraldata, false: spatialdata
    return spectral;
  }

  /*------------------------------------------------------------- toString ---*/

  public String toString() {    // @return information string for an ImageFFT object
    String s = new String(getClass().getName() + ": " + width + "x" + height +
     (spectral ? ", frequency domain" : ", spatial domain"));
    return s;
  }

  /*------------------------------------------------------------ transform ---*/

  public void transform() {

     /* Transforms data via a forward or inverse FFT, as appropriate.        */
     /* An inverse transform is computed if the previous transform was in    */
     /* the forward direction; otherwise, the forward transform is computed. */

    int x, y, i;
    Complex[] row = new Complex[width];
    for (x = 0; x < width; ++x)
      row[x] = new Complex();
    Complex[] column = new Complex[height];
    for (y = 0; y < height; ++y)
      column[y] = new Complex();

    int direction;
    if (spectral)
      direction = -1;   // inverse transform
    else
      direction = 1;    // forward transform


    for (y = 0; y < height; ++y) {               // Perform FFT on each row
      for (i = y*width, x = 0; x < width; ++x, ++i) {
        row[x].re = data[i].re;
        row[x].im = data[i].im;
      }
      reorder(row, width);
      fft(row, width, log2w, direction);
      for (i = y*width, x = 0; x < width; ++x, ++i) {
        data[i].re = row[x].re;
        data[i].im = row[x].im;
      }
    }



    for (x = 0; x < width; ++x) {                // Perform FFT on each column
      for (i = x, y = 0; y < height; ++y, i += width) {
        column[y].re = data[i].re;
        column[y].im = data[i].im;
      }
      reorder(column, height);
      fft(column, height, log2h, direction);
      for (i = x, y = 0; y < height; ++y, i += width) {
        data[i].re = column[y].re;
        data[i].im = column[y].im;
      }
    }

    if (spectral)
      spectral = false;
    else
      spectral = true;

  }

  /*-------------------------------------------------------------- toImage ---*/

  public BufferedImage toImage(BufferedImage image) throws FFTException {

         /* Converts stored data into an image.                          */
         /* @param image destination image, or null                      */
         /* @return FFT data as an image.                                */
         /* @exception FFTException if the data are in spectral form; an */
         /*  image can be created only from data in the spatial domain.  */

    return toImage(image, 0);
  }

  /*-------------------------------------------------------------- toImage ---*/

  public BufferedImage toImage(BufferedImage image, int bias)
   throws FFTException {

         /* Converts stored data into an image.                          */
         /* @param image destination image, or null                      */
         /* @param bias constant value added to data                     */
         /* @return FFT data as an image.                                */
         /* @exception FFTException if the data are in spectral form; an */
         /*  image can be created only from data in the spatial domain.  */

    if (spectral) {
      throw new FFTException("cannot transfer spectral data to an image");
    }

    if (image == null)
      image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    WritableRaster raster = image.getRaster();

    int w = Math.min(image.getWidth(), width);
    int h = Math.min(image.getHeight(), height);


    if (w < image.getWidth() || h < image.getHeight())  // If destination image is bigger, zero it
      for (int y = 0; y < image.getHeight(); ++y)
        for (int x = 0; x < image.getWidth(); ++x)
          raster.setSample(x, y, 0, 0);


    int i = 0, value;                                   // Copy real component of data to destination image
    for (int y = 0; y < height; ++y)
      for (int x = 0; x < width; ++x, ++i) {
        value = Math.max(0, Math.min(255, bias + Math.round(data[i].re)));
        raster.setSample(x, y, 0, value);
      }
    System.out.println("End toImage FFT " + image);
    return image;

  }

  /*---------------------------------------------------------- getSpectrum ---*/

  public BufferedImage getSpectrum() throws FFTException {

         /* Returns the amplitude spectrum of an image, as another image.   */
         /* The data are shifted such that the DC component is at the image */
         /* centre, and scaled logarithmically so that low-amplitude        */
         /* detail is visible.                                              */
         /* @return shifted spectrum, as an image.                          */
         /* @exception FFTException if spectral data are not available      */
         /* (e.g. because last transform was in the inverse direction).     */

    if (!spectral)
      throw new FFTException(NO_DATA);

    float[] magData = new float[width*height];   // Collect magnitudes and find maximum
    float maximum = calculateMagnitudes(magData);
    BufferedImage image =
     new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    WritableRaster raster = image.getRaster();

    double scale = 255.0 / Math.log(maximum + 1.0);   // Shift, rescale and copy to image
    int x2 = width/2, y2 = height/2;
    int sx, sy, value;
    for (int y = 0; y < height; ++y) {
      sy = shift(y, y2);
      for (int x = 0; x < width; ++x) {
        sx = shift(x, x2);
        value = (int) Math.round(scale*Math.log(magData[sy*width+sx]+1.0));
        raster.setSample(x, y, 0, value);
      }
    }

    return image;

  }

  /*------------------------------------------------- getUnshiftedSpectrum ---*/

  public BufferedImage getUnshiftedSpectrum() throws FFTException {

         /* Returns the amplitude spectrum of an image, as another image. */
         /* The data are unshifted and are scaled logarithmically so that */
         /* low-amplitude detail is visible.                              */
         /* @return unshifted spectrum, as an image.                      */
         /* @exception FFTException if spectral data are not available    */
         /* e.g. because last transform was in the inverse direction).    */

    if (!spectral)
      throw new FFTException(NO_DATA);

    float[] magData = new float[width*height];   // Collect magnitudes and find maximum
    float maximum = calculateMagnitudes(magData);
    BufferedImage image =
     new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    WritableRaster raster = image.getRaster();


    double scale = 255.0 / Math.log(maximum + 1.0); // Rescale and copy to image
    int i = 0, value;
    for (int y = 0; y < height; ++y)
      for (int x = 0; x < width; ++x, ++i) {
        value = (int) Math.round(scale*Math.log(magData[i]+1.0));
        raster.setSample(x, y, 0, value);
      }

    return image;

  }

  /*--------------------------------------------------------- getMagnitude ---*/

  public float getMagnitude(int u, int v) throws FFTException {

         /* Computes magnitude for any point in the spectrum.           */
         /* @param u horizontal spatial frequency                       */
         /* @param v vertical spatial frequency                         */
         /* @return magnitude at the specified point, or zero if point  */
         /* does not exist.                                             */
         /* @exception FFTException if spectral data are not available. */

    if (!spectral)
      throw new FFTException(NO_DATA);
    if (u >= 0 && u < width && v >= 0 && v < height)
      return data[v*width+u].getMagnitude();
    else
      return 0.0f;
  }

  /*------------------------------------------------------------- getPhase ---*/

  public float getPhase(int u, int v) throws FFTException {

    /* Computes phase for any point in the spectrum.                          */
    /* @param u horizontal spatial frequency                                  */
    /* @param v vertical spatial frequency                                    */
    /* @return phase at the specified point, or zero if point does not exist. */
    /* @exception FFTException if spectral data are not available.            */

    if (!spectral)
      throw new FFTException(NO_DATA);
    if (u >= 0 && u < width && v >= 0 && v < height)
      return data[v*width+u].getPhase();
    else
      return 0.0f;
  }

  /*--------------------------------------------------------- setMagnitude ---*/

  public void setMagnitude(int u, int v, float mag) throws FFTException {

         /* Modifies magnitude at any point in the spectrum.            */
         /* @param u horizontal spatial frequency                       */
         /* @param v vertical spatial frequency                         */
         /* @param mag new magnitiude for specified point               */
         /* @exception FFTException if spectral data are not available. */

    if (!spectral)
      throw new FFTException(NO_DATA);
    if (u >= 0 && u < width && v >= 0 && v < height) {
      int i = v*width+u;
      data[i].setPolar(mag, data[i].getPhase());
    }
  }

  /*------------------------------------------------------------- setPhase ---*/

  public void setPhase(int u, int v, float phase) throws FFTException {

         /* Modifies phase at any point in the spectrum.                */
         /* @param u horizontal spatial frequency                       */
         /* @param v vertical spatial frequency                         */
         /* @param phase new phase for specified point                  */
         /* @exception FFTException if spectral data are not available. */

    if (!spectral)
      throw new FFTException(NO_DATA);
    if (u >= 0 && u < width && v >= 0 && v < height) {
      int i = v*width+u;
      data[i].setPolar(data[i].getMagnitude(), phase);
    }
  }

  /*--------------------------------------------------- idealLowPassFilter ---*/

  public void idealLowPassFilter(double radius) throws FFTException {

         /* Performs ideal low pass filtering on the spectrum.         */
         /* @param radius filter radius                                */
         /* @exception FFTException if spectral data are not available */
         /*  or filter radius is invalid.                              */

    if (!spectral)
      throw new FFTException(NO_DATA);

    if (radius < 0.0 || radius > 1.0)
      throw new FFTException("invalid filter radius");

    int u2 = width/2;
    int v2 = height/2;
    int su, sv, i = 0;
    double r, rmax = Math.min(u2, v2);

    for (int v = 0; v < height; ++v) {
      sv = shift(v, v2) - v2;
      for (int u = 0; u < width; ++u, ++i) {
        su = shift(u, u2) - u2;
        r = Math.sqrt(su*su + sv*sv) / rmax;
        if (r > radius)
          data[i].re = data[i].im = 0.0f;
      }
    }

  }

  /*-------------------------------------------------- idealHighPassFilter ---*/

  public void idealHighPassFilter(double radius) throws FFTException {

         /* Performs ideal high pass filtering on the spectrum.        */
         /* @param radius filter radius                                */
         /* @exception FFTException if spectral data are not available */
         /*  or filter radius is invalid.                              */

    if (!spectral)
      throw new FFTException(NO_DATA);

    if (radius < 0.0 || radius > 1.0)
      throw new FFTException("invalid filter radius");

    int u2 = width/2;
    int v2 = height/2;
    int su, sv, i = 0;
    double r, rmax = Math.min(u2, v2);

    for (int v = 0; v < height; ++v) {
      sv = shift(v, v2) - v2;
      for (int u = 0; u < width; ++u, ++i) {
        su = shift(u, u2) - u2;
        r = Math.sqrt(su*su + sv*sv) / rmax;
        if (r < radius)
          data[i].re = data[i].im = 0.0f;
      }
    }

  }

  /*-------------------------------------------------- idealBandPassFilter ---*/

  public void idealBandPassFilter(double radius, double delta)
   throws FFTException {

         /* Performs ideal band pass filtering on the spectrum.        */
         /* @param radius filter radius                                */
         /* @param delta band width                                    */
         /* @exception FFTException if spectral data are not available */
         /*  or filter parameters are invalid.                         */

    if (!spectral)
      throw new FFTException(NO_DATA);

    double delta2 = delta/2.0;
    double r1 = radius - delta2;
    double r2 = radius + delta2;
    if (r1 < 0.0 || r2 > 1.0)
      throw new FFTException(INVALID_PARAMS);

    int u2 = width/2;
    int v2 = height/2;
    int su, sv, i = 0;
    double r, rmax = Math.min(u2, v2);

    for (int v = 0; v < height; ++v) {
      sv = shift(v, v2) - v2;
      for (int u = 0; u < width; ++u, ++i) {
        su = shift(u, u2) - u2;
        r = Math.sqrt(su*su + sv*sv) / rmax;
        if (r < r1 || r > r2)
          data[i].re = data[i].im = 0.0f;
      }
    }

  }

  /*-------------------------------------------------- idealBandStopFilter ---*/

  public void idealBandStopFilter(double radius, double delta)
   throws FFTException {

         /* Performs ideal band stop filtering on the spectrum.        */
         /* @param radius filter radius                                */
         /* @param delta band width                                    */
         /* @exception FFTException if spectral data are not available */
         /*  or filter parameters are invalid.                         */

    if (!spectral)
      throw new FFTException(NO_DATA);

    double delta2 = delta/2.0;
    double r1 = radius - delta2;
    double r2 = radius + delta2;
    if (r1 < 0.0 || r2 > 1.0)
      throw new FFTException(INVALID_PARAMS);

    int u2 = width/2;
    int v2 = height/2;
    int su, sv, i = 0;
    double r, rmax = Math.min(u2, v2);

    for (int v = 0; v < height; ++v) {
      sv = shift(v, v2) - v2;
      for (int u = 0; u < width; ++u, ++i) {
        su = shift(u, u2) - u2;
        r = Math.sqrt(su*su + sv*sv) / rmax;
        if (r >= r1 && r <= r2)
          data[i].re = data[i].im = 0.0f;
      }
    }

  }

  /*--------------------------------------------- butterworthLowPassFilter ---*/

  public void butterworthLowPassFilter(double radius) throws FFTException {

         /* Performs order-1 Butterworth low pass filtering of the spectrum. */
         /* @param radius filter radius                                      */
         /* @exception FFTException if spectral data are not available       */
         /*  or filter parameters are invalid.                               */

    butterworthLowPassFilter(1, radius);
  }

  /*--------------------------------------------- butterworthLowPassFilter ---*/

  public void butterworthLowPassFilter(int n, double radius)
   throws FFTException {

         /* Performs Butterworth low pass filtering of the spectrum.      */
         /* @param n order of filter                                      */
         /* @param radius filter radius                                   */
         /* @exception FFTException if spectral data are not available or */
         /* filter parameters are invalid.                                */

    if (!spectral)
      throw new FFTException(NO_DATA);

    if (n < 1 || radius <= 0.0 || radius > 1.0)
      throw new FFTException(INVALID_PARAMS);

    int u2 = width/2;
    int v2 = height/2;
    int su, sv, i = 0;
    double mag, r, rmax = Math.min(u2, v2);

    for (int v = 0; v < height; ++v) {
      sv = shift(v, v2) - v2;
      for (int u = 0; u < width; ++u, ++i) {
        su = shift(u, u2) - u2;
        r = Math.sqrt(su*su + sv*sv) / rmax;
        mag = butterworthLowPassFunction(n, radius, r)*data[i].getMagnitude();
        data[i].setPolar(mag, data[i].getPhase());
      }
    }

  }

  /*-------------------------------------------- butterworthHighPassFilter ---*/

  public void butterworthHighPassFilter(double radius) throws FFTException {

         /* Performs order-1 Butterworth high pass filtering of the spectrum. */
         /* @param radius filter radius                                       */
         /* @exception FFTException if spectral data are not available or     */
         /*  filter parameters are invalid.                                   */

    butterworthHighPassFilter(1, radius);
  }

  /*-------------------------------------------- butterworthHighPassFilter ---*/

  public void butterworthHighPassFilter(int n, double radius)
   throws FFTException {

         /* Performs Butterworth high pass filtering of the spectrum.     */
         /* @param n order of filter                                      */
         /* @param radius filter radius                                   */
         /* @exception FFTException if spectral data are not available or */
         /* filter parameters are invalid.                                */

    if (!spectral)
      throw new FFTException(NO_DATA);

    if (n < 1 || radius <= 0.0 || radius > 1.0)
      throw new FFTException(INVALID_PARAMS);

    int u2 = width/2;
    int v2 = height/2;
    int su, sv, i = 0;
    double mag, r, rmax = Math.min(u2, v2);

    for (int v = 0; v < height; ++v) {
      sv = shift(v, v2) - v2;
      for (int u = 0; u < width; ++u, ++i) {
        su = shift(u, u2) - u2;
        r = Math.sqrt(su*su + sv*sv) / rmax;
        mag = butterworthHighPassFunction(n, radius, r)*data[i].getMagnitude();
        data[i].setPolar(mag, data[i].getPhase());
      }
    }

  }

  /*-------------------------------------------- butterworthBandPassFilter ---*/

  public void butterworthBandPassFilter(double radius, double delta)
   throws FFTException {

         /* Performs order-1 Butterworth band pass filtering of the spectrum. */
         /* @param radius filter radius                                       */
         /* @param delta band width                                           */
         /* @exception FFTException if spectral data are not available or     */
         /* filter parameters are invalid.                                    */

    butterworthBandPassFilter(1, radius, delta);
  }

  /*-------------------------------------------- butterworthBandPassFilter ---*/

  public void butterworthBandPassFilter(int n, double radius, double delta)
   throws FFTException {

         /* Performs Butterworth band pass filtering of the spectrum.     */
         /* @param n order of filter                                      */
         /* @param radius filter radius                                   */
         /* @param delta band width                                       */
         /* @exception FFTException if spectral data are not available or */
         /*  filter parameters are invalid.                               */

    if (!spectral)
      throw new FFTException(NO_DATA);

    double delta2 = delta/2.0;
    if (n < 1 || radius-delta2 <= 0.0 || radius+delta2 > 1.0)
      throw new FFTException(INVALID_PARAMS);

    int u2 = width/2;
    int v2 = height/2;
    int su, sv, i = 0;
    double mag, r, rmax = Math.min(u2, v2);

    for (int v = 0; v < height; ++v) {
      sv = shift(v, v2) - v2;
      for (int u = 0; u < width; ++u, ++i) {
        su = shift(u, u2) - u2;
        r = Math.sqrt(su*su + sv*sv) / rmax;
        mag = butterworthBandPassFunction(n, radius, delta, r)
         * data[i].getMagnitude();
        data[i].setPolar(mag, data[i].getPhase());
      }
    }

  }

  /*---------------------------------------- butterworthBandStopPassFilter ---*/

  public void butterworthBandStopFilter(double radius, double delta)
   throws FFTException {

         /* Performs order-1 Butterworth band stop filtering of the spectrum. */
         /* @param radius filter radius                                       */
         /* @param delta band width                                           */
         /* @exception FFTException if spectral data are not available or     */
         /*  filter parameters are invalid.                                   */

    butterworthBandStopFilter(1, radius, delta);
  }

  /*---------------------------------------- butterworthBandStopPassFilter ---*/

  public void butterworthBandStopFilter(int n, double radius, double delta)
   throws FFTException {

         /* Performs Butterworth band stop filtering of the spectrum.     */
         /* @param n order of filter                                      */
         /* @param radius filter radius                                   */
         /* @param delta band width                                       */
         /* @exception FFTException if spectral data are not available or */
         /*  filter parameters are invalid.                               */

    if (!spectral)
      throw new FFTException(NO_DATA);

    double delta2 = delta/2.0;
    if (n < 1 || radius-delta2<= 0.0 || radius+delta2 > 1.0)
      throw new FFTException(INVALID_PARAMS);

    int u2 = width/2;
    int v2 = height/2;
    int su, sv, i = 0;
    double mag, r, rmax = Math.min(u2, v2);

    for (int v = 0; v < height; ++v) {
      sv = shift(v, v2) - v2;
      for (int u = 0; u < width; ++u, ++i) {
        su = shift(u, u2) - u2;
        r = Math.sqrt(su*su + sv*sv) / rmax;
        mag = butterworthBandStopFunction(n, radius, delta, r)
         * data[i].getMagnitude();
        data[i].setPolar(mag, data[i].getPhase());
      }
    }

  }

  /*----------------------------------------------------------- powerOfTwo ---*/

  private static int powerOfTwo(int n) {

          /* Computes the power of two for which the corresponding value */
          /* equals or exceeds the specified integer.                    */
          /* @param n integer value                                      */
          /* @return power of two                                        */

   int i = 0, m = 1;
   while (m < n) {
     m <<= 1;
     ++i;
   }
   return i;
  }

  /*-------------------------------------------------------------- reorder ---*/

  private static void reorder(Complex[] data, int n) {

          /* Reorders an array of data to prepare for an FFT.  The element   */
          /* at index i is swapped with the element at an index given by the */
          /* bit-reversed value of i.                                        */
          /* @param data array of complex values                             */
          /* @param n number of values                                       */

    int j = 0, m;
    for (int i = 0; i < n; ++i) {
      if (i > j)
        data[i].swapWith(data[j]);
      m = n >> 1;
      while ((j >= m) && (m >= 2)) {
        j -= m;
        m >>= 1;
      }
      j += m;
    }
  }

  /*------------------------------------------------------------------ fft ---*/

  private static void fft(Complex[] data, int size, int log2n, int dir) {

          /* Performs a one-dimensional FFT on the specified data.         */
          /* @param data input data, already reordered by bit-reversal     */
          /* @param size number of data elements                           */
          /* @param log2n base-2 logarithm of number of elements           */
          /* @param dir direction of transform (1 = forward, -1 = inverse) */

    double angle, wtmp, wpr, wpi, wr, wi, tmpr, tmpi;
    int n = 1, n2;
    for (int k = 0; k < log2n; ++k) {

      n2 = n;
      n <<= 1;
      angle = (-TWO_PI/n) * dir;
      wtmp = Math.sin(0.5*angle);
      wpr = -2.0*wtmp*wtmp;
      wpi = Math.sin(angle);
      wr = 1.0;
      wi = 0.0;

      for (int m = 0; m < n2; ++m) {
        for (int i = m; i < size; i += n) {
          int j = i + n2;
          tmpr = wr*data[j].re - wi*data[j].im;
          tmpi = wi*data[j].re + wr*data[j].im;
          data[j].re = (float) (data[i].re - tmpr);
          data[i].re += (float) tmpr;
          data[j].im = (float) (data[i].im - tmpi);
          data[i].im += (float) tmpi;
        }
        wtmp = wr;
        wr = wtmp*wpr - wi*wpi + wr;
        wi = wi*wpr + wtmp*wpi + wi;
      }

    }


    if (dir == -1)                     // Rescale results of inverse transform
      for (int i = 0; i < size; ++i) {
        data[i].re /= size;
        data[i].im /= size;
      }

  }

  /*---------------------------------------------------------------- shift ---*/

  private static final int shift(int d, int d2) {

          /* Shifts a coordinate relative to a centre point. */
          /* @param d coordinate                             */
          /* @param d2 centre point                          */
          /* @return shifted coordinate.                     */

    return (d >= d2 ? d-d2 : d+d2);
  }

  /*-------------------------------------------------- calculateMagnitudes ---*/

  private float calculateMagnitudes(float[] mag) {

          /* Collects magnitudes from spectral data, storing them */
          /* in the specified array.                              */
          /* @param mag destination for magnitudes                */
          /* @return maximum magnitude.                           */

    float maximum = 0.0f;
    for (int i = 0; i < data.length; ++i) {
      mag[i] = data[i].getMagnitude();
      if (mag[i] > maximum)
        maximum = mag[i];
    }
    return maximum;
  }

  /*--------------------------------------------------------------------------*/
}

/*============================================================================*/


