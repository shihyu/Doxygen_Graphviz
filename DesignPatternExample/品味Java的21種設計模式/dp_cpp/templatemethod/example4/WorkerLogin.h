#pragma once

#include "LoginTemplate.h"
#include "LoginModel.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace templatemethod
			{
				namespace example4
				{
					///
					/// <summary> * ������Ա��¼���Ƶ��߼����� </summary>
					/// 
					class WorkerLogin : public LoginTemplate
					{

					public:
						virtual LoginModel *findLoginUser(std::string loginId);
						virtual std::string encryptPwd(std::string pwd);
					};

				}
			}
		}
	}
}