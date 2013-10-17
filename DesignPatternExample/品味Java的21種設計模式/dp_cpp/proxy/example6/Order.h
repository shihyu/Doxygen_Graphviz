#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace proxy
			{
				namespace example6
				{
					///
					/// <summary> * �������� </summary>
					/// 
					class Order
					{
					///	
					///	 <summary> * ���������Ĳ�Ʒ���� </summary>
					///	 
					private:
						std::string productName;
					///	
					///	 <summary> * �������������� </summary>
					///	 
						int orderNum;
					///	
					///	 <summary> * ������������Ա </summary>
					///	 
						std::string orderUser;

					///	
					///	 <summary> * ���췽�������빹����Ҫ������ </summary>
					///	 * <param name="productName"> ���������Ĳ�Ʒ���� </param>
					///	 * <param name="orderNum"> �������������� </param>
					///	 * <param name="orderUser"> ������������Ա </param>
					///	 
					public:
						Order(std::string productName, int orderNum, std::string orderUser);

						virtual std::string getProductName();
						virtual void setProductName(std::string productName, std::string user);
						virtual int getOrderNum();
						virtual void setOrderNum(int orderNum, std::string user);
						virtual std::string getOrderUser();
						virtual void setOrderUser(std::string orderUser, std::string user);
					};

				}
			}
		}
	}
}