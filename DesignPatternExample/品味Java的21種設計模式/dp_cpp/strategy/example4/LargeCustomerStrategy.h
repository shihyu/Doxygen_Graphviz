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
					/// <summary> * �����㷨ʵ�֣�Ϊ��ͻ�����Ӧ���ļ۸� </summary>
					/// 
					class LargeCustomerStrategy : public Strategy
					{
					public:
						virtual double calcPrice(double goodsPrice);
					};

				}
			}
		}
	}
}