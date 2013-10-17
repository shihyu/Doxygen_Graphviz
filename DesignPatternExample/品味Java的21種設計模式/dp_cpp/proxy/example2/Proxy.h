#pragma once

#include "Subject.h"
#include "RealSubject.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace proxy
			{
				namespace example2
				{
					///
					/// <summary> * ������� </summary>
					/// 
					class Proxy : public Subject
					{
					///	
					///	 <summary> * ���б�����ľ����Ŀ����� </summary>
					///	 
					private:
						RealSubject *realSubject;
					///	
					///	 <summary> * ���췽�������뱻����ľ����Ŀ����� </summary>
					///	 * <param name="realSubject"> ������ľ����Ŀ����� </param>
					///	 
					public:
						Proxy(RealSubject *realSubject);

						virtual void request();

					};

				}
			}
		}
	}
}