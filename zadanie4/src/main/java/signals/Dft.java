package signals;

public class Dft {

        public static void computeDft(double[] inreal, double[] inimag, double[] outreal, double[] outimag) {
            int n = inreal.length;
            for (int k = 0; k < n; k++) {  // For each output element
                double sumreal = 0;
                double sumimag = 0;
                for (int t = 0; t < n; t++) {  // For each input element
                    double angle = 2 * Math.PI * t * k / n;
                    sumreal +=  inreal[t] * Math.cos(angle) + inimag[t] * Math.sin(angle);
                    sumimag += -inreal[t] * Math.sin(angle) + inimag[t] * Math.cos(angle);
                }
                outreal[k] = sumreal;
                outimag[k] = sumimag;
            }
        }

}
