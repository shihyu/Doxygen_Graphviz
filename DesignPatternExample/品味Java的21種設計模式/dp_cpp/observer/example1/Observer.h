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
				namespace example1
				{

					///
					/// <summary> * �۲��߽ӿڣ�����һ�����µĽӿڸ���Щ��Ŀ�귢���ı��ʱ��֪ͨ�Ķ��� </summary>
					/// 
					class Observer
					{
					///	
					///	 <summary> * ���µĽӿ� </summary>
					///	 * <param name="subject"> ����Ŀ����󣬺û�ȡ��Ӧ��Ŀ������״̬ </param>
					///	 
					public:
						virtual public void update(Subject*) = 0;

					};


				}
			}
		}
	}
}