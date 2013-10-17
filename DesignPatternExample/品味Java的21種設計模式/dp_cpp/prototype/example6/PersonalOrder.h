#pragma once

#include "OrderApi.h"
#include "Product.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace prototype
			{
				namespace example6
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
					///	 <summary> * ��Ʒ���� </summary>
					///	 
						Product *product;
					///	
					///	 <summary> * ������Ʒ���� </summary>
					///	 
						int orderProductNum;

					public:
						virtual int getOrderProductNum();
						virtual void setOrderProductNum(int num);
						virtual std::string getCustomerName();
						virtual void setCustomerName(std::string customerName);
						virtual Product *getProduct();
						virtual void setProduct(Product *product);

						virtual std::string ToString();

						virtual OrderApi *cloneOrder();
					};

				}
			}
		}
	}
}