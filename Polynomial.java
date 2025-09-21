package il.ac.tau.cs.sw1.polynomial;


public class Polynomial {
	private int len;
	private double [] coefficient;
	/*
	 * Creates the zero-polynomial with p(x) = 0 for all x.
	 */
	public Polynomial()
	{
		 this.coefficient =new double [0];
	} 
	/*
	 * Creates a new polynomial with the given coefficients.
	 */
	public Polynomial(double[] coefficients) 
	{
		this.coefficient = coefficients;
		this.len = coefficients.length;
	}
	/*
	 * Addes this polynomial to the given one
	 *  and retruns the sum as a new polynomial.
	 */
	
	public Polynomial adds (Polynomial polynomial)
	{
		int maxlen = Math.max(this.len , polynomial.len);
		double [] new_coe = new double [maxlen]; 
		for (int i = 0; i < this.len; i++)
		{
			new_coe [i] += this.coefficient[i];
		}
		
		for (int j = 0; j < polynomial.len; j++) 
		{
			new_coe [j] += polynomial.coefficient[j];
		}
		
		return new Polynomial(new_coe);
	
	}
	/*
	 * Multiplies a to this polynomial and returns 
	 * the result as a new polynomial.
	 */
	public Polynomial multiply(double a)
	{
		double [] new_coe = new double [this.len];
		
		for (int i = 0; i < this.len; i++) 
		{
			new_coe [i] = this.coefficient[i] * a;
		}
		
		
		return new Polynomial(new_coe);
	}	
	/*
	 * Returns the degree (the largest exponent) of this polynomial.
	 */
	public int getDegree()
	{
		
		for (int i = this.len - 1; i >= 0; i--) 
		{
			if(this.coefficient[i] != 0) 
			{
				return i;
			}
		}
		
		return 0;
	}	
	/*
	 * Returns the coefficient of the variable x 
	 * with degree n in this polynomial.
	 */
	public double getCoefficient(int n)
	{
		if (n >= this.len || n < 0) 
		{
			return 0;
		}
		else 
		{
			return this.coefficient[n];
		}
	}

	/*
	 * set the coefficient of the variable x 
	 * with degree n to c in this polynomial.
	 * If the degree of this polynomial < n, it means that that the coefficient of the variable x 
	 * with degree n was 0, and now it will change to c. 
	 */
	public void setCoefficient(int n, double c) 
	{
	    if (n < this.len) {
	        this.coefficient[n] = c;
	    } else {
	        double[] new_coe = new double[n + 1]; 
	        for (int j = 0; j < this.len; j++) {
	            new_coe[j] = this.coefficient[j];
	        }
	        for (int i = this.len; i < n; i++) { 
	            new_coe[i] = 0;
	        }
	        new_coe[n] = c; 
	        this.coefficient = new_coe;
	        this.len = n + 1;
	    }
	}

	
	/*
	 * Returns the first derivation of this polynomial.
	 *  The first derivation of a polynomal a0x0 + ...  + anxn is defined as 1 * a1x0 + ... + n anxn-1.
	 */
	public Polynomial getFirstDerivation() 
	{
	    if (this.len <= 1) {
	        return new Polynomial(new double[]{0});
	    }

	    double[] newCoeffs = new double[this.len - 1];

	    for (int i = 1; i < this.len; i++) {
	        newCoeffs[i - 1] = this.coefficient[i] * i;
	    }

	    return new Polynomial(newCoeffs);
	}

	
	/*
	 * given an assignment for the variable x,
	 * compute the polynomial value
	 */
	public double computePolynomial(double x)
	{
		double result = 0;
		double power =1;
	
		for (int i = 0; i < this.len; i++) 
		{
			result += this.coefficient[i] * power;
			power *= x;
		}
		
		return result;
		
	}
	
	/*
	 * given an assignment for the variable x,
	 * return true iff x is an extrema point (local minimum or local maximum of this polynomial)
	 * x is an extrema point if and only if The value of first derivation of a polynomal at x is 0
	 * and the second derivation of a polynomal value at x is not 0.
	 */
	public boolean isExtrema(double x)
	{
	    Polynomial firstDeriv = this.getFirstDerivation();
	    Polynomial secondDeriv = firstDeriv.getFirstDerivation();

	    double firstDerivValue = firstDeriv.computePolynomial(x);
	    double secondDerivValue = secondDeriv.computePolynomial(x);

	    return firstDerivValue == 0 && secondDerivValue != 0;
	}

	
	
	
	

    
    

}
