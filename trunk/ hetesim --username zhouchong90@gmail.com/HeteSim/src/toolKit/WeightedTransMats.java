package toolKit;

import java.util.ArrayList;

import calHeteSim.QuickHeteSim;

import model.Data;
import model.TransitiveMatrix;

public class WeightedTransMats
{
	private Data	data;

	public void WeightedTranMats(Data data)
	{
		this.data = data;
	}

	public TransitiveMatrix calWeightedMat(ArrayList<String> heteSimPaths)
	{
		// calPathsWeights,Ȼ���ۼ�
		ArrayList<Double> weights = calPathWeights(heteSimPaths);//�������·����Ȩֵ
		
		ArrayList<TransitiveMatrix> mats = new ArrayList<TransitiveMatrix>();
		
		for(String path : heteSimPaths)//����·���ϵĸ��ʾ���
		{
			QuickHeteSim qhs = new QuickHeteSim();
			TransitiveMatrix mat = qhs.getTransitiveMatrix(path);
			mats.add(mat);
		}
		
		TransitiveMatrix foo = new TransitiveMatrix();
		foo.weightedPlus(mats, weights);
		
		return null;
	}

	/**
	 * Given a list of paths, return the weights of each path respectively.
	 * 
	 * @param heteSimPaths
	 * @return
	 */

	private ArrayList<Double> calPathWeights(ArrayList<String> heteSimPaths)
	{
		// importanceOfOnePath(every), ͳ��ƽ��
		ArrayList<Double> rawWeights = new ArrayList<Double>();
		
		for(String str: heteSimPaths)
		{
			rawWeights.add(importanceOfOnePath(str));
		}
		
		double totalImportance = 0;
		for(Double Ii : rawWeights)
		{
			totalImportance += Ii;
		}
		
		ArrayList<Double> fineWeights = new ArrayList<Double>();
		for(Double I: rawWeights)
		{
			fineWeights.add( I/totalImportance );
		}
		
		return fineWeights;
	}

	/**
	 * calculate the importance of one Path using formula
	 * @param path
	 * @return
	 */
	private double importanceOfOnePath(String path)
	{
		// strengthOfOnePath��lengthOfOnePath�ĺ���
		double result;
		
		result = Math.pow(Math.E, strengthOfOnePath(path)-lengthOfOnePath(path));
		
		return result;
	}

	/**
	 * get S of a path, i.e. A,P,C
	 * @param path
	 * @return
	 */
	private double strengthOfOnePath(String path)
	{
		// strengthOfOneMat����
		String [] tmp = path.split(",");
		//error
		if(tmp.length < 2)
			throw new IllegalArgumentException("path too short!");
		
		double result = 1;
		for (int i = 0; i < tmp.length-1; i++)
		{
			result *= strengthOfOneMat(tmp[i] + "-" + tmp[i+1]); 
		}
		return result;
	}

	/**
	 * the strength of One TransitiveMatrix, it's a combination of average out
	 * degree of row and average in degree of col
	 * 
	 * @param matName  i.e A-P
	 * @return
	 */
	private double strengthOfOneMat(String matName)
	{
		// ��ʽ
		TransitiveMatrix mat = data.getTransMat(matName);
		
		double S = mat.getRowAvgOutDegree() * mat.getColAvgInDegree();
		
		S = Math.pow(S, -0.5);
		
		return S;
	}

	/**
	 * the length of One Path�� i.e A��P��C is length 2
	 * 
	 * @param path
	 * @return
	 */
	private double lengthOfOnePath(String path)
	{
		// �ַ�������
		return path.split(",").length - 1;
	}
}
