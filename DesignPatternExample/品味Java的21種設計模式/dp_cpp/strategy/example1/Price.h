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
				namespace example1
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
					};

				}
			}
		}
	}
}