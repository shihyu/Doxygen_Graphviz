#pragma once

#include "DepUserMediatorImpl.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace mediator
			{
				namespace example3
				{

					///
					/// <summary> * ��Ա�� </summary>
					/// 
					class User
					{
					///	
					///	 <summary> * ��Ա��� </summary>
					///	 
					private:
						std::string userId;
					///	
					///	 <summary> * ��Ա���� </summary>
					///	 
						std::string userName;
					public:
						virtual std::string getUserId();
						virtual void setUserId(std::string userId);
						virtual std::string getUserName();
						virtual void setUserName(std::string userName);
					///	
					///	 <summary> * ��Ա��ְ </summary>
					///	 * <returns> �Ƿ���ɹ� </returns>
					///	 
						virtual bool dimission();
					};

				}
			}
		}
	}
}