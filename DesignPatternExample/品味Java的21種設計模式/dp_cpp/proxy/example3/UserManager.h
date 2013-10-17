#pragma once

#include "UserModelApi.h"
#include "UserModel.h"
#include "Proxy.h"
#include <string>
#include <vector>

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
					/// <summary> * ʵ��ʾ��Ҫ��Ĺ��� </summary>
					/// 
					class UserManager
					{
					///	
					///	 <summary> * ���ݲ��ű������ȡ�ò����µ�������Ա </summary>
					///	 * <param name="depId"> ���ű�� </param>
					///	 * <returns> �ò����µ�������Ա </returns>
					///	 
					public:
						virtual Collection<UserModelApi*> *getUserByDepId(std::string depId) throw(Exception);
					///	
					///	 <summary> * ��ȡ�����ݿ������ </summary>
					///	 * <returns> ���ݿ����� </returns>
					///	 
					private:
						Connection *getConnection() throw(Exception);
					};

				}
			}
		}
	}
}