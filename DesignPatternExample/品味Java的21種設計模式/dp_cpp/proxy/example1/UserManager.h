#pragma once

#include "UserModel.h"
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
				namespace example1
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
						virtual Collection<UserModel*> *getUserByDepId(std::string depId) throw(Exception);
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