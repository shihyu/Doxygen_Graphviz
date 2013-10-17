#pragma once

#include "LoginTemplate.h"
#include "LoginModel.h"
#include "NormalLoginModel.h"
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
					/// <summary> * ��ͨ�û���¼���Ƽ�ǿ����߼����� </summary>
					/// 
					class NormalLogin2 : public LoginTemplate
					{
					public:
						virtual LoginModel *findLoginUser(std::string loginId);
						virtual bool match(LoginModel *lm, LoginModel *dbLm);

					};

				}
			}
		}
	}
}