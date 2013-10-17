#pragma once

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
					/// <summary> * ��װ���е�¼��������Ҫ�����ݣ��ڹ������ݵĻ����ϣ�
					/// * ��Ӿ���ģ����Ҫ������ </summary>
					/// 
					class NormalLoginModel : public LoginModel
					{
					///	
					///	 <summary> * ������֤���� </summary>
					///	 
					private:
						std::string question;
					///	
					///	 <summary> * ������֤�� </summary>
					///	 
						std::string answer;
					public:
						virtual std::string getQuestion();
						virtual void setQuestion(std::string question);
						virtual std::string getAnswer();
						virtual void setAnswer(std::string answer);

					};

				}
			}
		}
	}
}