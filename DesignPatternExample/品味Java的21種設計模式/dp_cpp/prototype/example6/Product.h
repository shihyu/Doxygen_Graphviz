#pragma once

#include "ProductPrototype.h"
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
					/// <summary> * ��Ʒ���� </summary>
					/// 
					class Product : public ProductPrototype
					{
					///	
					///	 <summary> * ��Ʒ��� </summary>
					///	 
					private:
						std::string productId;
					///	
					///	 <summary> * ��Ʒ���� </summary>
					///	 
						std::string name;


					public:
						virtual std::string getName();
						virtual void setName(std::string name);
						virtual std::string getProductId();
						virtual void setProductId(std::string productId);
						virtual std::string ToString();
						virtual ProductPrototype *cloneProduct();
					};

				}
			}
		}
	}
}