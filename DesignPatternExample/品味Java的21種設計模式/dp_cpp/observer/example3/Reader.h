#pragma once

#include "Observer.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace observer
			{
				namespace example3
				{
					///
					/// <summary> * �����Ķ��ߣ�Ϊ�˼򵥾�����һ������ </summary>
					/// 
					class Reader : public Observer
					{
					///	
					///	 <summary> * ���ߵ����� </summary>
					///	 
					private:
						std::string name;

					public:
						virtual void update(std::string content);
						virtual std::string getName();
						virtual void setName(std::string name);
					};

				}
			}
		}
	}
}