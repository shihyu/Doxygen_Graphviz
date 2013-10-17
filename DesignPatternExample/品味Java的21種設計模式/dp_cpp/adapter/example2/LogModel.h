#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace adapter
			{
				namespace example2
				{


					///
					/// <summary> * ��־���ݶ��� </summary>
					/// 
					class LogModel : public Serializable
					{
					///	
					///	 <summary> * ��־��� </summary>
					///	 
					private:
						std::string logId;
					///	
					///	 <summary> * ������Ա </summary>
					///	 
						std::string operateUser;
					///	
					///	 <summary> * ����ʱ�䣬��yyyy-MM-dd HH:mm:ss�ĸ�ʽ��¼ </summary>
					///	 
						std::string operateTime;
					///	
					///	 <summary> * ��־���� </summary>
					///	 
						std::string logContent;

					public:
						virtual std::string getLogId();
						virtual void setLogId(std::string logId);
						virtual std::string getOperateUser();
						virtual void setOperateUser(std::string operateUser);
						virtual std::string getOperateTime();
						virtual void setOperateTime(std::string operateTime);
						virtual std::string getLogContent();
						virtual void setLogContent(std::string logContent);

						virtual std::string ToString();
					};

				}
			}
		}
	}
}