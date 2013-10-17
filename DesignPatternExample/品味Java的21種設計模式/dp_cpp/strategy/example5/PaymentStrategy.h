#pragma once

#include "PaymentContext.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace strategy
			{
				namespace example5
				{
					///
					/// <summary> * ֧�����ʵĲ��ԵĽӿڣ���˾�ж���֧�����ʵ��㷨
					/// * ���磺�ֽ����п����ֽ�ӹ�Ʊ���ֽ����Ȩ����Ԫ֧���ȵ� </summary>
					/// 
					class PaymentStrategy
					{
					///	
					///	 <summary> * ��˾��ĳ������֧������ </summary>
					///	 * <param name="ctx"> ֧�����ʵ������ģ���������㷨��Ҫ������ </param>
					///	 
					public:
						virtual public void pay(PaymentContext*) = 0;
					};

				}
			}
		}
	}
}