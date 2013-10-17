#pragma once

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
					/// <summary> * ֧�����ʵ������ģ�ÿ���˵Ĺ��ʲ�ͬ��֧����ʽҲ��ͬ </summary>
					/// 
					class PaymentContext
					{
					///	
					///	 <summary> * Ӧ��֧�����ʵ���Ա���򵥵㣬������������ </summary>
					///	 
					private:
						std::string userName;
					///	
					///	 <summary> * Ӧ��֧���Ĺ��ʵĽ�� </summary>
					///	 
						double money;
					///	
					///	 <summary> * ֧�����ʵķ�ʽ���ԵĽӿ� </summary>
					///	 
						PaymentStrategy *strategy;
					///	
					///	 <summary> * ���췽�������뱻֧�����ʵ���Ա��Ӧ֧���Ľ��;����֧������ </summary>
					///	 * <param name="userName"> ��֧�����ʵ���Ա </param>
					///	 * <param name="money"> Ӧ֧���Ľ�� </param>
					///	 * <param name="strategy"> �����֧������ </param>
					///	 
					public:
						PaymentContext(std::string userName, double money, PaymentStrategy *strategy);

					///	
					///	 <summary> * ����֧������ </summary>
					///	 
						virtual void payNow();
						virtual std::string getUserName();

						virtual double getMoney();

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								money = 0.0;

								initialized = true;
							}
						}
					};

				}
			}
		}
	}
}