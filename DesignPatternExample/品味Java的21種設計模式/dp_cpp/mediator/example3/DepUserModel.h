#pragma once

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
					/// <summary> *	�������ź���Ա��ϵ���� </summary>
					/// 
					class DepUserModel
					{
					///	
					///	 <summary> * ���ڲ��ź���Ա��ϵ�ı�ţ��������� </summary>
					///	 
					private:
						std::string depUserId;
					///	
					///	 <summary> * ���ŵı�� </summary>
					///	 
						std::string depId;
					///	
					///	 <summary> * ��Ա�ı�� </summary>
					///	 
						std::string userId;
					public:
						virtual std::string getDepUserId();
						virtual void setDepUserId(std::string depUserId);
						virtual std::string getDepId();
						virtual void setDepId(std::string depId);
						virtual std::string getUserId();
						virtual void setUserId(std::string userId);
					};

				}
			}
		}
	}
}