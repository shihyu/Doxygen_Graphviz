#pragma once

#include "Observer.h"
#include "Subject.h"
#include "NewsPaper.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace observer
			{
				namespace example2
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
						virtual void update(Subject *subject);
						virtual std::string getName();
						virtual void setName(std::string name);
					};

				}
			}
		}
	}
}