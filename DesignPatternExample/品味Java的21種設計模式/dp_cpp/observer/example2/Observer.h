#pragma once

#include "Subject.h"

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
					/// <summary> * �۲��ߣ����籨ֽ�Ķ��� </summary>
					/// 
					class Observer
					{
					///	
					///	 <summary> * ��֪ͨ�ķ��� </summary>
					///	 * <param name="subject"> �����Ŀ����󣬿��Ի�ȡ��ֽ������ </param>
					///	 
					public:
						virtual public void update(Subject*) = 0;
					};

				}
			}
		}
	}
}