#pragma once

#include "OrderApi.h"
#include <string>

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
					/// <summary> * ���˶������� </summary>
					/// 
					class PersonalOrder : public OrderApi
					{
					///	
					///	 <summary> * ������Ա���� </summary>
					///	 
					private:
						std::string customerName;
					///	
					///	 <summary> * ��Ʒ��� </summary>
					///	 
						std::string productId;
					///	
					///	 <summary> * ������Ʒ���� </summary>
					///	 
						int orderProductNum;

					public:
						virtual int getOrderProductNum();
						virtual void setOrderProductNum(int num);
						virtual std::string getCustomerName();
						virtual void setCustomerName(std::string customerName);
						virtual std::string getProductId();
						virtual void setProductId(std::string productId);
						virtual std::string ToString();

						virtual OrderApi *cloneOrder();
					};

				}
			}
		}
	}
}