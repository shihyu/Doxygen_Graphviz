#pragma once

#include "LoginModel.h"
#include "LoginTemplate.h"

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
					/// <summary> * ��¼���Ƶ�ģ�巽����Ҫ�Ļص��ӿڣ�
					/// * ��Ҫ�����ܵİ�������Ҫ�Ľӿڷ������������,
					/// * ����˵�����п��Ա���չ�ķ�������Ҫ��������� </summary>
					/// 
					class LoginCallback
					{
					///	
					///	 <summary> * ���ݵ�¼��������Һͻ�ȡ�洢����Ӧ������ </summary>
					///	 * <param name="loginId"> ��¼��� </param>
					///	 * <returns> ��¼����ڴ洢�����Ӧ������ </returns>
					///	 
					public:
						virtual public LoginModel *findLoginUser(std::string) = 0;
					///	
					///	 <summary> * ���������ݽ��м��� </summary>
					///	 * <param name="pwd"> �������� </param>
					///	 * <param name="template"> LoginTemplate����ͨ������������
					///	 * 				LoginTemplate�ж���Ĺ���������ȱʡʵ�� </param>
					///	 * <returns> ���ܺ���������� </returns>
					///	 
						virtual public std::string encryptPwd(std::string, LoginTemplate*) = 0;
					///	
					///	 <summary> * �ж��û���д�ĵ�¼���ݺʹ洢�ж�Ӧ�������Ƿ�ƥ����� </summary>
					///	 * <param name="lm"> �û���д�ĵ�¼���� </param>
					///	 * <param name="dbLm"> �ڴ洢�ж�Ӧ������ </param>
					///	 * <param name="template"> LoginTemplate����ͨ������������
					///	 * 				LoginTemplate�ж���Ĺ���������ȱʡʵ�� </param>
					///	 * <returns> true��ʾƥ��ɹ���false��ʾƥ��ʧ�� </returns>
					///	 
						virtual public bool match(LoginModel*, LoginModel*, LoginTemplate*) = 0;
					};

				}
			}
		}
	}
}