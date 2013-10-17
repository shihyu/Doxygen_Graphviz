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
					/// <summary> * ��ҵ�������� </summary>
					/// 
					class EnterpriseOrder : public OrderApi
					{
					///	
					///	 <summary> * ��ҵ���� </summary>
					///	 
					private:
						std::string enterpriseName;
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
						virtual std::string getEnterpriseName();
						virtual void setEnterpriseName(std::string enterpriseName);
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