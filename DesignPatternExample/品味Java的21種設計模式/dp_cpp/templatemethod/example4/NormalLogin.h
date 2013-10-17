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
					/// <summary> * ��ͨ�û���¼���Ƶ��߼����� </summary>
					/// 
					class NormalLogin : public LoginTemplate
					{
					public:
						virtual LoginModel *findLoginUser(std::string loginId);
					};

				}
			}
		}
	}
}