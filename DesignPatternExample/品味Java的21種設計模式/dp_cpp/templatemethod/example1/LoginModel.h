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
				namespace example1
				{
					///
					/// <summary> * ������¼��Ա��¼ʱ��д����Ϣ������ģ�� </summary>
					/// 
					class LoginModel
					{
					private:
						std::string userId, pwd;

					public:
						virtual std::string getUserId();

						virtual void setUserId(std::string userId);

						virtual std::string getPwd();

						virtual void setPwd(std::string pwd);

					};

				}
			}
		}
	}
}