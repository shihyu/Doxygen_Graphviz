#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace flyweight
			{
				namespace example1
				{
					///
					/// <summary> * ������Ȩ���ݵ�����model </summary>
					/// 
					class AuthorizationModel
					{
					///	
					///	 <summary> * ��Ա </summary>
					///	 
					private:
						std::string user;
					///	
					///	 <summary> * ��ȫʵ�� </summary>
					///	 
						std::string securityEntity;
					///	
					///	 <summary> * Ȩ�� </summary>
					///	 
						std::string permit;

					public:
						virtual std::string getUser();
						virtual void setUser(std::string user);
						virtual std::string getSecurityEntity();
						virtual void setSecurityEntity(std::string securityEntity);
						virtual std::string getPermit();
						virtual void setPermit(std::string permit);

					};

				}
			}
		}
	}
}