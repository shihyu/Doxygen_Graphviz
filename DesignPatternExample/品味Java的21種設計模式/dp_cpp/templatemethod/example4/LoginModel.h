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
				namespace example4
				{
					///
					/// <summary> * ��װ���е�¼��������Ҫ������ </summary>
					/// 
					class LoginModel
					{
					///	
					///	 <summary> * ��¼��Ա�ı�ţ�ͨ�õģ��������û���ţ�Ҳ�����ǹ�����Ա��� </summary>
					///	 
					private:
						std::string loginId;
					///	
					///	 <summary> * ��¼������ </summary>
					///	 
						std::string pwd;
					public:
						virtual std::string getLoginId();
						virtual void setLoginId(std::string loginId);
						virtual std::string getPwd();
						virtual void setPwd(std::string pwd);
					};

				}
			}
		}
	}
}