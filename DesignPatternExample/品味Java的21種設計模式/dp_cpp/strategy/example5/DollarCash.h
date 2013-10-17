#pragma once

#include "PaymentStrategy.h"
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
					/// <summary> * ��Ԫ�ֽ�֧�� </summary>
					/// 
					class DollarCash : public PaymentStrategy
					{

					public:
						virtual void pay(PaymentContext *ctx);
					};
				}
			}
		}
	}
}