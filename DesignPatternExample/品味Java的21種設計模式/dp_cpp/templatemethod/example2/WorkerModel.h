#pragma once

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
					/// <summary> * ����������Ա��Ϣ������ģ�� </summary>
					/// 
					class WorkerModel
					{
					private:
						std::string uuid, workerId, pwd, name;
					public:
						virtual std::string getUuid();
						virtual void setUuid(std::string uuid);
						virtual std::string getWorkerId();
						virtual void setWorkerId(std::string workerId);
						virtual std::string getPwd();
						virtual void setPwd(std::string pwd);
						virtual std::string getName();
						virtual void setName(std::string name);
					};
				}
			}
		}
	}
}