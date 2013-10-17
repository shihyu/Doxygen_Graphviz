#pragma once

#include "Strategy.h"

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
					/// <summary> * �۸������Ҫ��ɼ�����ͻ������۸�Ĺ��� </summary>
					/// 
					class Price
					{
					///	
					///	 <summary> * ����һ������Ĳ��Զ��� </summary>
					///	 
					private:
						Strategy *strategy;
					///	
					///	 <summary> * ���췽��������һ������Ĳ��Զ��� </summary>
					///	 * <param name="aStrategy"> ����Ĳ��Զ��� </param>
					///	 
					public:
						Price(Strategy *aStrategy);
					///	
					///	 <summary> * ���ۣ�����Կͻ��ı��� </summary>
					///	 * <param name="goodsPrice"> ��Ʒ����ԭ�� </param>
					///	 * <returns> ��������ģ�Ӧ�ø��ͻ����ļ۸� </returns>
					///	 
						virtual double quote(double goodsPrice);
					};

				}
			}
		}
	}
}