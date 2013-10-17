#pragma once

#include "PaymentStrategy.h"
#include "PaymentContext.h"
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
					/// <summary> * ֧�������п� </summary>
					/// 
					class Card2 : public PaymentStrategy
					{
					///	
					///	 <summary> * �ʺ���Ϣ </summary>
					///	 
					private:
						std::string account;
					///	
					///	 <summary> * ���췽���������ʺ���Ϣ </summary>
					///	 * <param name="account"> �ʺ���Ϣ </param>
					///	 
					public:
						Card2(std::string account);
						virtual void pay(PaymentContext *ctx);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								account = "";

								initialized = true;
							}
						}
					};

				}
			}
		}
	}
}