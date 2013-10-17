#pragma once

#include "UserModelApi.h"
#include "UserModel.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace proxy
			{
				namespace example3
				{


					///
					/// <summary> * �������,�����û����ݶ��� </summary>
					/// 
					class Proxy : public UserModelApi
					{
					///	
					///	 <summary> * ���б�����ľ����Ŀ����� </summary>
					///	 
					private:
						UserModel *realSubject;
					///	
					///	 <summary> * ���췽�������뱻����ľ����Ŀ����� </summary>
					///	 * <param name="realSubject"> ������ľ����Ŀ����� </param>
					///	 
					public:
						Proxy(UserModel *realSubject);
					///	
					///	 <summary> * ��ʾ�Ƿ��Ѿ�����װ�ع������� </summary>
					///	 
					private:
						bool loaded;


					public:
						virtual std::string getUserId();
						virtual void setUserId(std::string userId);
						virtual std::string getName();
						virtual void setName(std::string name);


						virtual void setDepId(std::string depId);
						virtual void setSex(std::string sex);

						virtual std::string getDepId();
						virtual std::string getSex();

					///	
					///	 <summary> * ���²�ѯ���ݿ��Ի�ȡ�������û����� </summary>
					///	 
					private:
						void reload();
					public:
						virtual std::string ToString();

					private:
						Connection *getConnection() throw(Exception);
					};

				}
			}
		}
	}
}