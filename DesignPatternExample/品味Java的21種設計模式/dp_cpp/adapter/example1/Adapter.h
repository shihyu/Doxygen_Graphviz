#pragma once

#include "Target.h"
#include "Adaptee.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace adapter
			{
				namespace example1
				{
					///
					/// <summary> * ������ </summary>
					/// 
					class Adapter : public Target
					{
					///	
					///	 <summary> * ������Ҫ������Ľӿڶ��� </summary>
					///	 
					private:
						Adaptee *adaptee;
					///	
					///	 <summary> * ���췽����������Ҫ������Ķ��� </summary>
					///	 * <param name="adaptee"> ��Ҫ������Ķ��� </param>
					///	 
					public:
						Adapter(Adaptee *adaptee);

						virtual void request();
					};


				}
			}
		}
	}
}