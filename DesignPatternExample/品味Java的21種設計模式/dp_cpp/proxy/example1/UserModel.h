#pragma once

#include <string>

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
					/// <summary> * �����û����ݵĶ��� </summary>
					/// 
					class UserModel
					{
					///	
					///	 <summary> * �û���� </summary>
					///	 
					private:
						std::string userId;
					///	
					///	 <summary> * �û����� </summary>
					///	 
						std::string name;
					///	
					///	 <summary> * ���ű�� </summary>
					///	 
						std::string depId;
					///	
					///	 <summary> * �Ա� </summary>
					///	 
						std::string sex;

					public:
						virtual std::string getUserId();
						virtual void setUserId(std::string userId);
						virtual std::string getName();
						virtual void setName(std::string name);
						virtual std::string getDepId();
						virtual void setDepId(std::string depId);
						virtual std::string getSex();
						virtual void setSex(std::string sex);

						virtual std::string ToString();
					};

				}
			}
		}
	}
}