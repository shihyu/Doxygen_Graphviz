#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace state
			{
				namespace example8
				{
					///
					/// <summary> * ��ٵ����� </summary>
					/// 
					class LeaveRequestModel
					{
					///	
					///	 <summary> * ����� </summary>
					///	 
					private:
						std::string user;
					///	
					///	 <summary> * ��ٿ�ʼʱ�� </summary>
					///	 
						std::string beginDate;
					///	
					///	 <summary> * ������� </summary>
					///	 
						int leaveDays;
					///	
					///	 <summary> * ��˽�� </summary>
					///	 
						std::string result;

					public:
						virtual std::string getResult();

						virtual void setResult(std::string result);

						virtual std::string getUser();

						virtual std::string getBeginDate();

						virtual int getLeaveDays();
						virtual void setUser(std::string user);

						virtual void setBeginDate(std::string beginDate);

						virtual void setLeaveDays(int leaveDays);
					};

				}
			}
		}
	}
}