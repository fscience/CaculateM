package com.fscience.caculate;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class GLMatrix {
	
	/*double m00, m01, m02, m03;
	double m10, m11, m12, m13;
	double m20, m21, m22, m23;
	double m30, m31, m32, m33;
	
	public GLMatrix() {
		this.identity();
	}
	
	public void identity() {
		m00 = m11 = m22 = m33 = 1;
		
		m01 = m02 = m03 = m10 = m12 = m13 = m20 = m21 = m23 = m30 = m31 = m32 = 0;
	}
	
	public void multiply(GLMatrix matrix) {
		m00 = m00 * matrix.m00 + m10 * matrix.m01 + m20 * matrix.m02 + m30 * matrix.m03;
		m10 = m00 * matrix.m10 + m10 * matrix.m11 + m20 * matrix.m12 + m30 * matrix.m13;
		m20 = m00 * matrix.m20 + m10 * matrix.m21 + m20 * matrix.m22 + m30 * matrix.m23;
		m30 = m00 * matrix.m30 + m10 * matrix.m31 + m20 * matrix.m32 + m30 * matrix.m33;
		
		m01 = m01 * matrix.m00 + m11 * matrix.m01 + m21 * matrix.m02 + m31 * matrix.m03;
		m11 = m01 * matrix.m10 + m11 * matrix.m11 + m21 * matrix.m12 + m31 * matrix.m13;
		m21 = m01 * matrix.m20 + m11 * matrix.m21 + m21 * matrix.m22 + m31 * matrix.m23;
		m31 = m01 * matrix.m30 + m11 * matrix.m31 + m21 * matrix.m32 + m31 * matrix.m33;
		
		m02 = m02 * matrix.m00 + m12 * matrix.m01 + m22 * matrix.m02 + m32 * matrix.m03;
		m12 = m02 * matrix.m10 + m12 * matrix.m11 + m22 * matrix.m12 + m32 * matrix.m13;
		m22 = m02 * matrix.m20 + m12 * matrix.m21 + m22 * matrix.m22 + m32 * matrix.m23;
		m32 = m02 * matrix.m30 + m12 * matrix.m31 + m22 * matrix.m32 + m32 * matrix.m33;
		
		m03 = m03 * matrix.m00 + m13 * matrix.m01 + m23 * matrix.m02 + m33 * matrix.m03;
		m13 = m03 * matrix.m10 + m13 * matrix.m11 + m23 * matrix.m12 + m33 * matrix.m13;
		m23 = m03 * matrix.m20 + m13 * matrix.m21 + m23 * matrix.m22 + m33 * matrix.m23;
		m33 = m03 * matrix.m30 + m13 * matrix.m31 + m23 * matrix.m32 + m33 * matrix.m33;
	}
	
	public void translate(double x, double y, double z) {
		m30 += m00 * x + m10 * y + m20 * z;
		m31 += m01 * x + m11 * y + m21 * z;
		m32 += m02 * x + m12 * y + m22 * z;
		
//		GLMatrix matrix = new GLMatrix();
//		matrix.m30 = x;
//		matrix.m31 = y;
//		matrix.m32 = z;
//		this.multiply(matrix);
	}
	public void translate(MPoint vector) {
		this.translate(vector.x, vector.y, vector.z);
	}
	
	public void scale(double sx, double sy, double sz) {
		m00 *= sx; m01 *= sx; m02 *= sx; m03 *= sx;
		m10 *= sy; m11 *= sy; m12 *= sy; m13 *= sy;
		m20 *= sz; m21 *= sz; m22 *= sz; m23 *= sz;
		
//		GLMatrix matrix = new GLMatrix();
//		matrix.m00 = sx;
//		matrix.m11 = sy;
//		matrix.m22 = sz;
//		this.multiply(matrix);
	}
	
	public void rotateX(double radians) {
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		
		GLMatrix m = new GLMatrix();
		m.m11 = m.m22 = cos;
		m.m12 = sin;
		m.m21 = -sin;
		
		this.multiply(m);
	}
	
	public void rotateY(double radians) {
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		
		GLMatrix m = new GLMatrix();
		m.m00 = m.m22 = cos;
		m.m02 = -sin;
		m.m20 = sin;
		
		this.multiply(m);
	}
	
	public void rotateZ(double radians) {
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		
		GLMatrix m = new GLMatrix();
		m.m00 = m.m11 = cos;
		m.m01 = sin;
		m.m10 = -sin;
		
		this.multiply(m);
	}
	
	public void rotate(double radians, double x, double y, double z) {
		
	}
	
	public GLVector multiplyVector(GLVector vector) {
		double x = m00 * vector.x + m10 * vector.y + m20 * vector.z + m30 * vector.w;
		double y = m01 * vector.x + m11 * vector.y + m21 * vector.z + m31 * vector.w;
		double z = m02 * vector.x + m12 * vector.y + m22 * vector.z + m32 * vector.w;
		double w = m03 * vector.x + m13 * vector.y + m23 * vector.z + m33 * vector.w;
		
		return new GLVector(x, y, z, w);
	}
	
	public MPoint multiplyPoint(MPoint point) {
		double x = m00 * point.x + m10 * point.y + m20 * point.z + m30;
		double y = m01 * point.x + m11 * point.y + m21 * point.z + m31;
		double z = m02 * point.x + m12 * point.y + m22 * point.z + m32;
		
		return new MPoint(x, y, z);
	}
	
	public GLMatrix invert() {
		return invertGeneral(this);
	}
	
	final GLMatrix invertGeneral(GLMatrix paramMatrix4d) {
		double[] arrayOfDouble1 = new double[16];
		int[] arrayOfInt = new int[4];

		double[] arrayOfDouble2 = new double[16];

		arrayOfDouble2[0] = paramMatrix4d.m00;
		arrayOfDouble2[1] = paramMatrix4d.m01;
		arrayOfDouble2[2] = paramMatrix4d.m02;
		arrayOfDouble2[3] = paramMatrix4d.m03;

		arrayOfDouble2[4] = paramMatrix4d.m10;
		arrayOfDouble2[5] = paramMatrix4d.m11;
		arrayOfDouble2[6] = paramMatrix4d.m12;
		arrayOfDouble2[7] = paramMatrix4d.m13;

		arrayOfDouble2[8] = paramMatrix4d.m20;
		arrayOfDouble2[9] = paramMatrix4d.m21;
		arrayOfDouble2[10] = paramMatrix4d.m22;
		arrayOfDouble2[11] = paramMatrix4d.m23;

		arrayOfDouble2[12] = paramMatrix4d.m30;
		arrayOfDouble2[13] = paramMatrix4d.m31;
		arrayOfDouble2[14] = paramMatrix4d.m32;
		arrayOfDouble2[15] = paramMatrix4d.m33;

		if (!luDecomposition(arrayOfDouble2, arrayOfInt)) {
			// throw new Exception("Matrix4d10");
			return null;
		}

		for (int i = 0; i < 16; i++)
			arrayOfDouble1[i] = 0.0D;
		arrayOfDouble1[0] = 1.0D;
		arrayOfDouble1[5] = 1.0D;
		arrayOfDouble1[10] = 1.0D;
		arrayOfDouble1[15] = 1.0D;
		luBacksubstitution(arrayOfDouble2, arrayOfInt, arrayOfDouble1);

		GLMatrix matrix = new GLMatrix();
		matrix.m00 = arrayOfDouble1[0];
		matrix.m01 = arrayOfDouble1[1];
		matrix.m02 = arrayOfDouble1[2];
		matrix.m03 = arrayOfDouble1[3];

		matrix.m10 = arrayOfDouble1[4];
		matrix.m11 = arrayOfDouble1[5];
		matrix.m12 = arrayOfDouble1[6];
		matrix.m13 = arrayOfDouble1[7];

		matrix.m20 = arrayOfDouble1[8];
		matrix.m21 = arrayOfDouble1[9];
		matrix.m22 = arrayOfDouble1[10];
		matrix.m23 = arrayOfDouble1[11];

		matrix.m30 = arrayOfDouble1[12];
		matrix.m31 = arrayOfDouble1[13];
		matrix.m32 = arrayOfDouble1[14];
		matrix.m33 = arrayOfDouble1[15];
		return matrix;
	}

	static boolean luDecomposition(double[] paramArrayOfDouble,
			int[] paramArrayOfInt) {
		double[] arrayOfDouble = new double[4];

		int d2 = 0;
		int j = 0;

		int d1 = 4;
		double d3;
		while (d1-- != 0) {
			d3 = 0.0D;

			int i = 4;
			while (i-- != 0) {
				double d4 = paramArrayOfDouble[(d2++)];
				d4 = Math.abs(d4);
				if (d4 > d3) {
					d3 = d4;
				}

			}

			if (d3 == 0.0D) {
				return false;
			}
			arrayOfDouble[(j++)] = (1.0D / d3);
		}

		int i = 0;

		for (d1 = 0; d1 < 4; d1++) {
			int m;
			double d5;
			int n;
			int i1;
			int k;
			for (d2 = 0; d2 < d1; d2++) {
				m = i + 4 * d2 + d1;
				d5 = paramArrayOfDouble[m];
				d3 = d2;
				n = i + 4 * d2;
				i1 = i + d1;
				while (d3-- != 0) {
					d5 -= paramArrayOfDouble[n] * paramArrayOfDouble[i1];
					n++;
					i1 += 4;
				}
				paramArrayOfDouble[m] = d5;
			}

			double d6 = 0.0D;
			j = -1;
			double d7;
			for (d2 = d1; d2 < 4; d2++) {
				m = i + 4 * d2 + d1;
				d5 = paramArrayOfDouble[m];
				k = d1;
				n = i + 4 * d2;
				i1 = i + d1;
				while (k-- != 0) {
					d5 -= paramArrayOfDouble[n] * paramArrayOfDouble[i1];
					n++;
					i1 += 4;
				}
				paramArrayOfDouble[m] = d5;

				if ((d7 = arrayOfDouble[d2] * Math.abs(d5)) >= d6) {
					d6 = d7;
					j = d2;
				}
			}

			if (j < 0) {
				// throw new
				// RuntimeException(VecMathI18N.getString("Matrix4d11"));
				System.out.println("Matrix4 Failed....");
			}

			if (d1 != j) {
				k = 4;
				n = i + 4 * j;
				i1 = i + 4 * d1;
				while (k-- != 0) {
					d7 = paramArrayOfDouble[n];
					paramArrayOfDouble[(n++)] = paramArrayOfDouble[i1];
					paramArrayOfDouble[(i1++)] = d7;
				}

				arrayOfDouble[j] = arrayOfDouble[d1];
			}

			paramArrayOfInt[d1] = j;

			if (paramArrayOfDouble[(i + 4 * d1 + d1)] == 0.0D) {
				return false;
			}

			if (d1 != 3) {
				d7 = 1.0D / paramArrayOfDouble[(i + 4 * d1 + d1)];
				m = i + 4 * (d1 + 1) + d1;
				d2 = 3 - d1;
				while (d2-- != 0) {
					paramArrayOfDouble[m] *= d7;
					m += 4;
				}
			}

		}

		return true;
	}

	static void luBacksubstitution(double[] paramArrayOfDouble1,
			int[] paramArrayOfInt, double[] paramArrayOfDouble2) {
		int i1 = 0;

		for (int n = 0; n < 4; n++) {
			int i2 = n;
			int j = -1;

			for (int i = 0; i < 4; i++) {
				int k = paramArrayOfInt[(i1 + i)];
				double d = paramArrayOfDouble2[(i2 + 4 * k)];
				paramArrayOfDouble2[(i2 + 4 * k)] = paramArrayOfDouble2[(i2 + 4 * i)];
				if (j >= 0) {
					int i3 = i * 4;
					for (int m = j; m <= i - 1; m++) {
						d -= paramArrayOfDouble1[(i3 + m)]
								* paramArrayOfDouble2[(i2 + 4 * m)];
					}
				}
				if (d != 0.0D) {
					j = i;
				}
				paramArrayOfDouble2[(i2 + 4 * i)] = d;
			}

			int i3 = 12;
			paramArrayOfDouble2[(i2 + 12)] /= paramArrayOfDouble1[(i3 + 3)];

			i3 -= 4;
			paramArrayOfDouble2[(i2 + 8)] = ((paramArrayOfDouble2[(i2 + 8)] - paramArrayOfDouble1[(i3 + 3)]
					* paramArrayOfDouble2[(i2 + 12)]) / paramArrayOfDouble1[(i3 + 2)]);

			i3 -= 4;
			paramArrayOfDouble2[(i2 + 4)] = ((paramArrayOfDouble2[(i2 + 4)]
					- paramArrayOfDouble1[(i3 + 2)]
					* paramArrayOfDouble2[(i2 + 8)] - paramArrayOfDouble1[(i3 + 3)]
					* paramArrayOfDouble2[(i2 + 12)]) / paramArrayOfDouble1[(i3 + 1)]);

			i3 -= 4;
			paramArrayOfDouble2[(i2 + 0)] = ((paramArrayOfDouble2[(i2 + 0)]
					- paramArrayOfDouble1[(i3 + 1)]
					* paramArrayOfDouble2[(i2 + 4)]
					- paramArrayOfDouble1[(i3 + 2)]
					* paramArrayOfDouble2[(i2 + 8)] - paramArrayOfDouble1[(i3 + 3)]
					* paramArrayOfDouble2[(i2 + 12)]) / paramArrayOfDouble1[(i3 + 0)]);
		}
	}*/
	
	Matrix4d matrix = null;
	public GLMatrix() {
		matrix = new Matrix4d();
		matrix.setIdentity();
	}
	
	public void identity() {
		matrix.setIdentity();
	}
	public void multiply(GLMatrix matrix) {
	}
	public void translate(double x, double y, double z) {
		Matrix4d tmpMatrix = new Matrix4d();
		tmpMatrix.setIdentity();
		tmpMatrix.setTranslation(new Vector3d(x, y, z));
		
		matrix.mul(tmpMatrix);
	}
	public void scale(double sx, double sy, double sz) {
		Matrix4d tmpMatrix = new Matrix4d();
		tmpMatrix.setIdentity();
		tmpMatrix.set(sx);
		
		matrix.mul(tmpMatrix);
	}
	public void rotateX(double radians) {
		Matrix4d tmpMatrix = new Matrix4d();
		tmpMatrix.setIdentity();
		tmpMatrix.rotX(radians);
		
		matrix.mul(tmpMatrix);
	}
	public void rotateY(double radians) {
		Matrix4d tmpMatrix = new Matrix4d();
		tmpMatrix.setIdentity();
		tmpMatrix.rotY(radians);
		
		matrix.mul(tmpMatrix);
	}
	public void rotateZ(double radians) {
		Matrix4d tmpMatrix = new Matrix4d();
		tmpMatrix.setIdentity();
		tmpMatrix.rotZ(radians);
		
		matrix.mul(tmpMatrix);
	}
	public GLMatrix invert() {
		GLMatrix invertMatrix = new GLMatrix();
		invertMatrix.matrix.invert(matrix);
		
		return invertMatrix;
	}
	
	public MPoint multiplyPoint(MPoint point) {
		Point3d point3D = new Point3d(point.x, point.y, point.z);
		matrix.transform(point3D);
		return new MPoint(point3D.x, point3D.y, point3D.z);
	}
}
