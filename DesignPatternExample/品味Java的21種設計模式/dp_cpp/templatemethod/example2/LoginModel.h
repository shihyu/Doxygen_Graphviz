#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace templatemethod
			{
				namespace example2
				{
					///
					/// <summary> * ������¼��Ա��¼ʱ��д����Ϣ������ģ�� </summary>
					/// 
					class LoginModel
					{
					private:
						std::string workerId, pwd;
					public:
						virtual std::string getWorkerId();
						virtual void setWorkerId(std::string workerId);
						virtual std::string getPwd();
						virtual void setPwd(std::string pwd);
					};

				}
			}
		}
	}
}