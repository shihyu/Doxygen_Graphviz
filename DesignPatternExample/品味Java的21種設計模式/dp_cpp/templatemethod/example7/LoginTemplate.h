#pragma once

#include "LoginModel.h"
#include "LoginCallback.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace templatemethod
			{
				namespace example7
				{
					///
					/// <summary> *	��¼���Ƶ�ģ�� </summary>
					/// 
					class LoginTemplate
					{
					///	
					///	 <summary> * �жϵ�¼�����Ƿ���ȷ��Ҳ�����Ƿ��ܵ�¼�ɹ� </summary>
					///	 * <param name="lm"> ��װ��¼���ݵ�Model </param>
					///	 * <param name="callback"> LoginCallback���� </param>
					///	 * <returns> true��ʾ��¼�ɹ���false��ʾ��¼ʧ�� </returns>
					///	 
					public:
						bool login(LoginModel *lm, LoginCallback *callback);
					///	
					///	 <summary> * ���������ݽ��м��� </summary>
					///	 * <param name="pwd"> �������� </param>
					///	 * <returns> ���ܺ���������� </returns>
					///	 
						virtual std::string encryptPwd(std::string pwd);
					///	
					///	 <summary> * �ж��û���д�ĵ�¼���ݺʹ洢�ж�Ӧ�������Ƿ�ƥ����� </summary>
					///	 * <param name="lm"> �û���д�ĵ�¼���� </param>
					///	 * <param name="dbLm"> �ڴ洢�ж�Ӧ������ </param>
					///	 * <returns> true��ʾƥ��ɹ���false��ʾƥ��ʧ�� </returns>
					///	 
						virtual bool match(LoginModel *lm, LoginModel *dbLm);
					};

				}
			}
		}
	}
}