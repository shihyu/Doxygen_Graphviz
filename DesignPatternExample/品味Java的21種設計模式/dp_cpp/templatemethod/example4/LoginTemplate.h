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
					/// <summary> *	��¼���Ƶ�ģ�� </summary>
					/// 
					class LoginTemplate
					{
					///	
					///	 <summary> * �жϵ�¼�����Ƿ���ȷ��Ҳ�����Ƿ��ܵ�¼�ɹ� </summary>
					///	 * <param name="lm"> ��װ��¼���ݵ�Model </param>
					///	 * <returns> true��ʾ��¼�ɹ���false��ʾ��¼ʧ�� </returns>
					///	 
					public:
						bool login(LoginModel *lm);
					///	
					///	 <summary> * ���ݵ�¼��������Һͻ�ȡ�洢����Ӧ������ </summary>
					///	 * <param name="loginId"> ��¼��� </param>
					///	 * <returns> ��¼����ڴ洢�����Ӧ������ </returns>
					///	 
						virtual LoginModel *findLoginUser(std::string loginId) = 0;
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