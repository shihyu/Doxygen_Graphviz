#pragma once

#include "PaymentContext.h"
#include "PaymentStrategy.h"
#include <string>

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
					/// <summary> * ��չ��֧�������Ķ��� </summary>
					/// 
					class PaymentContext2 : public PaymentContext
					{
					///	
					///	 <summary> * �����ʺ� </summary>
					///	 
					private:
						std::string account;
					///	
					///	 <summary> * ���췽�������뱻֧�����ʵ���Ա��Ӧ֧���Ľ��;����֧������ </summary>
					///	 * <param name="userName"> ��֧�����ʵ���Ա </param>
					///	 * <param name="money"> Ӧ֧���Ľ�� </param>
					///	 * <param name="account"> ֧�����������ʺ� </param>
					///	 * <param name="strategy"> �����֧������ </param>
					///	 
					public:
						PaymentContext2(std::string userName, double money, std::string account, PaymentStrategy *strategy);
						virtual std::string getAccount();
					};

				}
			}
		}
	}
}