#pragma once

#include "LoginModel.h"
#include "UserModel.h"
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
					/// <summary> * ��ͨ�û���¼���Ƶ��߼����� </summary>
					/// 
					class NormalLogin
					{
					///	
					///	 <summary> * �жϵ�¼�����Ƿ���ȷ��Ҳ�����Ƿ��ܵ�¼�ɹ� </summary>
					///	 * <param name="lm"> ��װ��¼���ݵ�Model </param>
					///	 * <returns> true��ʾ��¼�ɹ���false��ʾ��¼ʧ�� </returns>
					///	 
					public:
						virtual bool login(LoginModel *lm);
					///	
					///	 <summary> * �����û���Ż�ȡ�û�����ϸ��Ϣ </summary>
					///	 * <param name="userId"> �û���� </param>
					///	 * <returns> ��Ӧ���û�����ϸ��Ϣ </returns>
					///	 
					private:
						UserModel *findUserByUserId(std::string userId);
					};

				}
			}
		}
	}
}