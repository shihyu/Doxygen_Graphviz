#pragma once

#include "OrderApi.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace prototype
			{
				namespace example3
				{
					///
					/// <summary> * ��������ҵ����� </summary>
					/// 
					class OrderBusiness
					{
					///	
					///	 <summary> * ���������ķ��� </summary>
					///	 * <param name="order"> �����Ľӿڶ��� </param>
					///	 
					public:
						virtual void saveOrder(OrderApi *order);
					};
				}
			}
		}
	}
}