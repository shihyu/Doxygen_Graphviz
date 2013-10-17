#pragma once

#include "Customer.h"
#include "Visitor.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example4
				{
					///
					/// <summary> * ���˿ͻ� </summary>
					/// 
					class PersonalCustomer : public Customer
					{
					///	
					///	 <summary> * ��ϵ�绰 </summary>
					///	 
					private:
						std::string telephone;
					///	
					///	 <summary> * ���� </summary>
					///	 
						int age;

					public:
						virtual std::string getTelephone();
						virtual void setTelephone(std::string telephone);
						virtual int getAge();
						virtual void setAge(int age);
						virtual void accept(Visitor *visitor);
					};
				}
			}
		}
	}
}