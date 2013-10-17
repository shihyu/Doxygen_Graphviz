#pragma once

#include "OrderApi.h"
#include "Order.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace proxy
			{
				namespace example4
				{

					///
					/// <summary> * �����Ĵ������ </summary>
					/// 
					class OrderProxy : public OrderApi
					{
					///	
					///	 <summary> * ���б�����ľ����Ŀ����� </summary>
					///	 
					private:
						Order *order;
					///	
					///	 <summary> * ���췽�������뱻����ľ����Ŀ����� </summary>
					///	 * <param name="realSubject"> ������ľ����Ŀ����� </param>
					///	 
					public:
						OrderProxy(Order *realSubject);
						virtual void setProductName(std::string productName, std::string user);
						virtual void setOrderNum(int orderNum, std::string user);
						virtual void setOrderUser(std::string orderUser, std::string user);
						virtual int getOrderNum();
						virtual std::string getOrderUser();
						virtual std::string getProductName();
						virtual std::string ToString();
					};

				}
			}
		}
	}
}