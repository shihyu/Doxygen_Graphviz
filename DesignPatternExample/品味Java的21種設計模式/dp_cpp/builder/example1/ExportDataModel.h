#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace builder
			{
				namespace example1
				{
					///
					/// <summary> * ����������ݵĶ��� </summary>
					/// 
					class ExportDataModel
					{
					///	
					///	 <summary> * ��Ʒ��� </summary>
					///	 
					private:
						std::string productId;
					///	
					///	 <summary> * ���ۼ۸� </summary>
					///	 
						double price;
					///	
					///	 <summary> * �������� </summary>
					///	 
						double amount;

					public:
						virtual std::string getProductId();
						virtual void setProductId(std::string productId);
						virtual double getPrice();
						virtual void setPrice(double price);
						virtual double getAmount();
						virtual void setAmount(double amount);

					};

				}
			}
		}
	}
}