#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace strategy
			{
				namespace example4
				{

					///
					/// <summary> * ���ԣ�������㱨���㷨�Ľӿ� </summary>
					/// 
					class Strategy
					{
					///	
					///	 <summary> * ����Ӧ���ļ۸� </summary>
					///	 * <param name="goodsPrice"> ��Ʒ����ԭ�� </param>
					///	 * <returns> ��������ģ�Ӧ�ø��ͻ����ļ۸� </returns>
					///	 
					public:
						virtual public double calcPrice(double) = 0;
					};


				}
			}
		}
	}
}