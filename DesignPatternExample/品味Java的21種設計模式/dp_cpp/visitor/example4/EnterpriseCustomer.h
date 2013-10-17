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
					/// <summary> * ��ҵ�ͻ� </summary>
					/// 
					class EnterpriseCustomer : public Customer
					{
					///	
					///	 <summary> * ��ϵ�� </summary>
					///	 
					private:
						std::string linkman;
					///	
					///	 <summary> * ��ϵ�绰 </summary>
					///	 
						std::string linkTelephone;
					///	
					///	 <summary> * ��ҵע���ַ </summary>
					///	 
						std::string registerAddress;


					public:
						virtual std::string getLinkman();

						virtual void setLinkman(std::string linkman);

						virtual std::string getLinkTelephone();

						virtual void setLinkTelephone(std::string linkTelephone);

						virtual std::string getRegisterAddress();

						virtual void setRegisterAddress(std::string registerAddress);

						virtual void accept(Visitor *visitor);
					};

				}
			}
		}
	}
}