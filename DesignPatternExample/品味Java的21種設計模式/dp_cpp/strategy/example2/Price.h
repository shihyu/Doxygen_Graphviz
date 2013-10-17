#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace strategy
			{
				namespace example2
				{
					///
					/// <summary> * �۸������Ҫ��ɼ�����ͻ������۸�Ĺ��� </summary>
					/// 
					class Price
					{
					///	
					///	 <summary> * ���ۣ��Բ�ͬ���͵ģ����㲻ͬ�ļ۸� </summary>
					///	 * <param name="goodsPrice"> ��Ʒ����ԭ�� </param>
					///	 * <param name="customerType"> �ͻ����� </param>
					///	 * <returns> ��������ģ�Ӧ�ø��ͻ����ļ۸� </returns>
					///	 
					public:
						virtual double quote(double goodsPrice, std::string customerType);
					///	
					///	 <summary> * Ϊ�¿ͻ���������ͨ�ͻ�����Ӧ���ļ۸� </summary>
					///	 * <param name="goodsPrice"> ��Ʒ����ԭ�� </param>
					///	 * <returns> ��������ģ�Ӧ�ø��ͻ����ļ۸� </returns>
					///	 
					private:
						double calcPriceForNormal(double goodsPrice);
					///	
					///	 <summary> * Ϊ�Ͽͻ�����Ӧ���ļ۸� </summary>
					///	 * <param name="goodsPrice"> ��Ʒ����ԭ�� </param>
					///	 * <returns> ��������ģ�Ӧ�ø��ͻ����ļ۸� </returns>
					///	 
						double calcPriceForOld(double goodsPrice);
					///	
					///	 <summary> * Ϊ��ͻ�����Ӧ���ļ۸� </summary>
					///	 * <param name="goodsPrice"> ��Ʒ����ԭ�� </param>
					///	 * <returns> ��������ģ�Ӧ�ø��ͻ����ļ۸� </returns>
					///	 
						double calcPriceForLarge(double goodsPrice);
					};

				}
			}
		}
	}
}