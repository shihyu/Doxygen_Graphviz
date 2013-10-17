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
					/// <summary> * �����㷨ʵ�֣�Ϊս�Ժ����ͻ��ͻ�����Ӧ���ļ۸� </summary>
					/// 
					class CooperateCustomerStrategy : public Strategy
					{
					public:
						virtual double calcPrice(double goodsPrice);
					};

				}
			}
		}
	}
}