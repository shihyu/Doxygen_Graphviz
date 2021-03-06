#pragma once

#include "Customer.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example2
				{
					///
					/// <summary> * 个人客户 </summary>
					/// 
					class PersonalCustomer : public Customer
					{
					///	
					///	 <summary> * 联系电话 </summary>
					///	 
					private:
						std::string telephone;
					///	
					///	 <summary> * 年龄 </summary>
					///	 
						int age;

					public:
						virtual std::string getTelephone();

						virtual void setTelephone(std::string telephone);

						virtual int getAge();

						virtual void setAge(int age);


					///	
					///	 <summary> * 个人客户提出服务请求的方法，示意一下 </summary>
					///	 
						virtual void serviceRequest();
					///	
					///	 <summary> * 个人客户对公司产品的偏好分析，示意一下 </summary>
					///	 
						virtual void predilectionAnalyze();
					///	
					///	 <summary> * 个人客户价值分析，示意一下 </summary>
					///	 
						virtual void worthAnalyze();
					};
				}
			}
		}
	}
}