#pragma once

#include "LoginModel.h"
#include "WorkerModel.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace templatemethod
			{
				namespace example2
				{

					///
					/// <summary> * ������Ա��¼���Ƶ��߼����� </summary>
					/// 
					class WorkerLogin
					{
					///	
					///	 <summary> * �жϵ�¼�����Ƿ���ȷ��Ҳ�����Ƿ��ܵ�¼�ɹ� </summary>
					///	 * <param name="lm"> ��װ��¼���ݵ�Model </param>
					///	 * <returns> true��ʾ��¼�ɹ���false��ʾ��¼ʧ�� </returns>
					///	 
					public:
						virtual bool login(LoginModel *lm);
					///	
					///	 <summary> * ���������ݽ��м��� </summary>
					///	 * <param name="pwd"> �������� </param>
					///	 * <returns> ���ܺ���������� </returns>
					///	 
					private:
						std::string encryptPwd(std::string pwd);
					///	
					///	 <summary> * ���ݹ�����Ա��Ż�ȡ������Ա����ϸ��Ϣ </summary>
					///	 * <param name="workerId"> ������Ա��� </param>
					///	 * <returns> ��Ӧ�Ĺ�����Ա����ϸ��Ϣ </returns>
					///	 
						WorkerModel *findWorkerByWorkerId(std::string workerId);
					};

				}
			}
		}
	}
}